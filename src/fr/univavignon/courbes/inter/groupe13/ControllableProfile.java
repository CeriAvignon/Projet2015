package fr.univavignon.courbes.inter.groupe13;

import java.awt.event.KeyEvent;

import fr.univavignon.courbes.common.Profile;

public class ControllableProfile {
	
	private Profile profile;
	private KeyEvent left;
	private KeyEvent right;
	
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public KeyEvent getLeft() {
		return left;
	}
	public void setLeft(KeyEvent left) {
		this.left = left;
	}
	public KeyEvent getRight() {
		return right;
	}
	public void setRight(KeyEvent right) {
		this.right = right;
	}
	
	@Override
	public String toString(){
		return profile.userName;
	}

}
