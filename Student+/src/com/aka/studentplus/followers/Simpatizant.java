package com.aka.studentplus.followers;

public class Simpatizant {
	
	private String nume;
	private String prenume;
	private String telefon;
	private String email;
	
	public Simpatizant(String nume, String prenume, String telefon, String email) {
		super();
		this.nume = nume;
		this.prenume = prenume;
		this.telefon = telefon;
		this.email = email;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getPrenume() {
		return prenume;
	}

	public void setPrenume(String prenume) {
		this.prenume = prenume;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
