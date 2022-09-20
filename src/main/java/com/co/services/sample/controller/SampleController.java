package com.co.services.sample.controller;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

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
    
}
