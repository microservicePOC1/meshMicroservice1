package com.co.services.sample.controller;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.env.Environment;
import com.co.services.sample.models.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@RestController
public class SampleController {
  
     @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Logger log = LoggerFactory.getLogger(SampleController.class);
    MathContext m = new MathContext(5);
    
    private BigDecimal initValue(Double val) {
        return new BigDecimal(val).round(m);
    }
    
     @GetMapping("/")
     public String listSample() {
		String sql = "SELECT * FROM REV_LINE_ITEM";
		List<Map<String, Object>> sqlResults = jdbcTemplate.queryForList(sql);
		Map<Integer, ArrayList<BigDecimal>> results = new HashMap<Integer, ArrayList<BigDecimal>>();
		
		Integer i = 1;
		for(Map<String, Object> o : sqlResults) {
			results.put(i++,  new ArrayList<BigDecimal>(Arrays.asList(
				initValue((Double)o.get("P1")),
				initValue((Double)o.get("P2")),
				initValue((Double)o.get("P3")),
				initValue((Double)o.get("P4")),
				initValue((Double)o.get("P5")),
				initValue((Double)o.get("P6")),
				initValue((Double)o.get("P7")),
				initValue((Double)o.get("P8")),
				initValue((Double)o.get("P9")),
				initValue((Double)o.get("P10"))
        	)));
  		}

		// map each period year to the sum of its list of amounts
		Map<Integer, BigDecimal> totals = new HashMap<>();

		// sum the values
		for(Integer k : results.keySet()) {
			BigDecimal sum = BigDecimal.ZERO;
			for(BigDecimal d : results.get(k)) {
				sum = sum.add(d);
			}
			totals.put(k, sum.round(m));
		}
		String output = "";
		String lineBreak = "<br>";
		output += "Sample Recalc 7.3" + lineBreak + lineBreak;
		output += "Input" + lineBreak;

		for(Integer k : totals.keySet()) {
			output += "Period Id = " + k.toString() + lineBreak;
			for(BigDecimal d : results.get(k)) {
				output += "Esn Revenue = " + d.toString() + lineBreak;
			}
		}

		output += lineBreak + "Output" + lineBreak;

		for(Integer k : totals.keySet()) {
			output += "Period Id = " + k.toString();
			output += ", Esn Summed Revenue = " + totals.get(k).toString() + lineBreak;
		}

		return output;
	}
	
	  		//check for VALUE equal to requesting user generating new ms
	    @PostMapping("/msNameCheck")
	    public List<LookupResponse> checkMsName(@RequestParam("msNameRequested") String msNameRequested) {
	        Map<String, String> env = System.getenv();
	        System.out.println("DATA PASSED IS: "+ msNameRequested.toString());
	        // Java 8
	        env.forEach((k, v) -> System.out.println(k + ":" + v));

	        Map<String, String> linkedHashMap = new LinkedHashMap<>();
		boolean found = false;
		    
		for (Map.Entry<String, String> confEnvVars : env.entrySet()) {
	            if(confEnvVars.getKey().contains("CONFIG_")){
	                linkedHashMap.put(confEnvVars.getKey(), confEnvVars.getValue());
	            }
	        }
	        for (Map.Entry<String, String> confEnvVars : env.entrySet()) {
	            if(confEnvVars.getValue().equals(msNameRequested)){
			System.out.println("FOUND EXISTING MS NAME: " + msNameRequested);
	                found = true;
			break;
	            }
	        }
		if(found === true){
		            return Arrays.asList(new LookupResponse("Name" + "'" + msNameRequested + "'" +   " is already taken",false ));
		}
		else{
			    return Arrays.asList(new LookupResponse("Name" +"'" + msNameRequested + "'" +   " is available",true ));
		 }
		    

	    }
	    
	    //gets all PW prefixed envs, just adapt for 'CONF_' and test in dep
	    @GetMapping("/msNameMap")
	    public List<EnvMap> getMsNameMap() {
	        Map<String, String> env = System.getenv();
	        // Java 8
	        env.forEach((k, v) -> System.out.println(k + ":" + v));
//	        String ms1test = environment.getProperty("environment.CONF_MS0");
	        Map<String, String> linkedHashMap = new LinkedHashMap<>();

	        for (Map.Entry<String, String> confEnvVars : env.entrySet()) {
	            if(confEnvVars.getKey().contains("CONFIG_")){
	                linkedHashMap.put(confEnvVars.getKey(), confEnvVars.getValue());
	            }
	        }
	        //add hashmap to serializable pojo and return it
	        System.out.println("Filtered Map: " + linkedHashMap);

	        
	        return Arrays.asList(new EnvMap(linkedHashMap));
	    }
	
	
    
}
