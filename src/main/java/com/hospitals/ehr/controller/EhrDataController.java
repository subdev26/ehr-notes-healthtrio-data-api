package com.hospitals.ehr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospitals.ehr.component.EhrApiConnect;
import com.hospitals.ehr.models.DataModel;
import com.hospitals.ehr.utils.EhrModelPercentageComparator;

@RestController
@RequestMapping("/data")
public class EhrDataController {

	@Autowired
	EhrApiConnect connect;

	@GetMapping("/api/{period}")
	public ResponseEntity<LinkedHashMap<String, HashMap<String, String>>> getEhrData(@PathVariable String period) {

		HashMap<String, HashMap<String, String>> outputMap = connect.ehrApiConnection(period);

		LinkedHashMap<String, HashMap<String, String>> outputResponse = new LinkedHashMap<String, HashMap<String, String>>();

//		System.out.println("Number of records: " + outputMap.size());

		List<DataModel> ehrInfo = new ArrayList<DataModel>();

		// Retrieving only keys from returned outputMap
		Set<String> outputKeys = outputMap.keySet();

		// For loop over the outputKeys
		for (String eachKey : outputKeys) {

			// Retrieving each inner-hashMap using each key
			HashMap<String, String> eachKeyMap = outputMap.get(eachKey);

			// Retrieving all keys of inner-hashMap
			Set<String> mapKeys = eachKeyMap.keySet();

			DataModel dataModel = new DataModel();
			int count = 0;
			float sum = 0;

			// For loop of the each key of inner-hashMap
			for (String eachMapKey : mapKeys) {

				// Looking for region in inner-hashMap
				if (eachMapKey.equals("region")) {
					dataModel.setState(eachMapKey);

				}
				// Looking for Basic EHR Notes fields
				else if (eachMapKey.contains("basic_ehr_notes")) {
					count++;
					String value = eachKeyMap.get(eachMapKey);
					// System.out.println(value);

					if (!value.equals(".") && value.length() > 0)
						sum = +Float.parseFloat(value);
				}
			}

			dataModel.setPercentage((sum / count) * 100);
			dataModel.setData(eachKeyMap);
			dataModel.setKey(eachKey);
			ehrInfo.add(dataModel);
		}

		ehrInfo.sort(new EhrModelPercentageComparator());
		// Cleaning up the memory
		outputMap.clear();

		for (DataModel dataModel : ehrInfo) {
			dataModel.getData().put("basic_ehr_notes_Percentage", String.valueOf(dataModel.getPercentage()));
			outputResponse.put(dataModel.getKey(), dataModel.getData());
		}

		return ResponseEntity.ok(outputResponse);
	}

}
