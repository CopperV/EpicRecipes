package me.Vark123.EpicRecipes.Utils;

import java.util.Optional;

import lombok.Getter;

@Getter
public class BooleanMessage {

	private BooleanAction action;
	private Optional<String> message;
	
	private BooleanMessage(BooleanAction action, String msg) {
		this.action = action;
		this.message = Optional.ofNullable(msg);
	}
	
	public static BooleanMessage ALLOW() {
		return ALLOW(null);
	}
	
	public static BooleanMessage ALLOW(String msg) {
		return new BooleanMessage(BooleanAction.ALLOW, msg);
	}
	
	public static BooleanMessage DENY() {
		return DENY(null);
	}
	
	public static BooleanMessage DENY(String msg) {
		return new BooleanMessage(BooleanAction.DENY, msg);
	}
	
}
