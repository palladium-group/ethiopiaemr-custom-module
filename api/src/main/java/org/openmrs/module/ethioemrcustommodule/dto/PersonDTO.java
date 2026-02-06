/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ethioemrcustommodule.dto;

import java.util.Date;
import java.util.List;

/**
 * DTO representing a person
 */
public class PersonDTO {
	
	private Integer age;
	
	private Boolean dead;
	
	private String uuid;
	
	private String gender;
	
	private Boolean voided;
	
	private String display;
	
	private Date birthdate;
	
	private Date birthtime;
	
	private Date deathDate;
	
	private List<Object> attributes;
	
	private Object causeOfDeath;
	
	private PreferredNameDTO preferredName;
	
	private String resourceVersion;
	
	private Object preferredAddress;
	
	private Boolean birthdateEstimated;
	
	private Boolean deathdateEstimated;
	
	public PersonDTO() {
	}
	
	public Integer getAge() {
		return age;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}
	
	public Boolean getDead() {
		return dead;
	}
	
	public void setDead(Boolean dead) {
		this.dead = dead;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public Boolean getVoided() {
		return voided;
	}
	
	public void setVoided(Boolean voided) {
		this.voided = voided;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public void setDisplay(String display) {
		this.display = display;
	}
	
	public Date getBirthdate() {
		return birthdate;
	}
	
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	public Date getBirthtime() {
		return birthtime;
	}
	
	public void setBirthtime(Date birthtime) {
		this.birthtime = birthtime;
	}
	
	public Date getDeathDate() {
		return deathDate;
	}
	
	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}
	
	public List<Object> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(List<Object> attributes) {
		this.attributes = attributes;
	}
	
	public Object getCauseOfDeath() {
		return causeOfDeath;
	}
	
	public void setCauseOfDeath(Object causeOfDeath) {
		this.causeOfDeath = causeOfDeath;
	}
	
	public PreferredNameDTO getPreferredName() {
		return preferredName;
	}
	
	public void setPreferredName(PreferredNameDTO preferredName) {
		this.preferredName = preferredName;
	}
	
	public String getResourceVersion() {
		return resourceVersion;
	}
	
	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}
	
	public Object getPreferredAddress() {
		return preferredAddress;
	}
	
	public void setPreferredAddress(Object preferredAddress) {
		this.preferredAddress = preferredAddress;
	}
	
	public Boolean getBirthdateEstimated() {
		return birthdateEstimated;
	}
	
	public void setBirthdateEstimated(Boolean birthdateEstimated) {
		this.birthdateEstimated = birthdateEstimated;
	}
	
	public Boolean getDeathdateEstimated() {
		return deathdateEstimated;
	}
	
	public void setDeathdateEstimated(Boolean deathdateEstimated) {
		this.deathdateEstimated = deathdateEstimated;
	}
}
