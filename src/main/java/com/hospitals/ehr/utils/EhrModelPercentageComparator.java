package com.hospitals.ehr.utils;

import java.util.Comparator;

import com.hospitals.ehr.models.DataModel;

public class EhrModelPercentageComparator implements Comparator<DataModel> {

	@Override
	public int compare(DataModel o1, DataModel o2) {

		if (o1.getPercentage() < o2.getPercentage()) {
			return 1;
		}
		;

		return -1;
	}

}
