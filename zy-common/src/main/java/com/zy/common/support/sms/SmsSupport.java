package com.zy.common.support.sms;

public interface SmsSupport {

	SmsResult send(String phone, String message, String sign);

	public static class SmsResult {
		private boolean success;
		private String message;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;

		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}

}





	
	
	
