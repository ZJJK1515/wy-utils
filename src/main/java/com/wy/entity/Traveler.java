package com.wy.entity;

public class Traveler {
	private Integer id;
	private String address;

	private String route;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	@Override
	public String toString() {
		return "Traveler{" +
			"id='" + id + '\'' +
			", address='" + address + '\'' +
			", route='" + route + '\'' +
			'}';
	}
}
