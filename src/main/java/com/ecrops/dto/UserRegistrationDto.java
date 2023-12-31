package com.ecrops.dto;


public class UserRegistrationDto {
	
	private String userid;
	private String district;
	private String  blockortehsil;
	private String village;
	private String encpassword;
	private String type_user;
	public String getUserid() {
		return userid;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getBlockortehsil() {
		return blockortehsil;
	}
	public void setBlockortehsil(String blockortehsil) {
		this.blockortehsil = blockortehsil;
	}
	public String getVillage() {
		return village;
	}
	public void setVillage(String village) {
		this.village = village;
	}
	public String getEncpassword() {
		return encpassword;
	}
	public void setEncpassword(String encpassword) {
		this.encpassword = encpassword;
	}
	public String getType_user() {
		return type_user;
	}
	public void setType_user(String type_user) {
		this.type_user = type_user;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	@Override
	public String toString() {
		return "UserRegistrationDto [userid=" + userid + ", district=" + district + ", blockortehsil=" + blockortehsil
				+ ", village=" + village + ", encpassword=" + encpassword + ", type_user=" + type_user
				+ ", getUserid()=" + getUserid() + ", getDistrict()=" + getDistrict() + ", getBlockortehsil()="
				+ getBlockortehsil() + ", getVillage()=" + getVillage() + ", getEncpassword()=" + getEncpassword()
				+ ", getType_user()=" + getType_user() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
}
