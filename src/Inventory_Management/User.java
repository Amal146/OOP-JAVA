package Inventory_Management;



public class User {
		private int userId;
	    private String username;
	    private String password;

	    public User(int userId, String username, String password ) {
	        this.setUserId(userId);
	        this.setUsername(username);
	        this.setPassword(password);
	        
	    }
	    public User(String username, String password) {
	        this.setUsername(username);
	        this.setPassword(password);
	    }
	    
	    public User(int userId, String username) {
	        this.setUsername(username);
	        this.userId = userId;
	    }
        
		public String getName() {
				return getUsername();
			}

		public void setName(String name) {
				this.setUsername(username);
			}
	    

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}

		
}
