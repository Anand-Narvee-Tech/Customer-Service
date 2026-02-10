package com.example.DTO;

public enum NetTerm {

	NET_7(7, "1 Week"), NET_14(14, "2 Weeks"), NET_30(30, "1 Month"), NET_45(45, "45 Days");

	private final int days;
	private final String label;

	NetTerm(int days, String label) {
		this.days = days;
		this.label = label;
	}

	public int getDays() {
		return days;
	}

	public String getLabel() {
		return label;
	}
}
