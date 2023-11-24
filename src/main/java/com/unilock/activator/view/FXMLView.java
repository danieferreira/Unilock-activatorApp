package com.unilock.activator.view;

import java.util.ResourceBundle;

public enum FXMLView {
    MAIN {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("main.app.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Main.fxml";
        }
    }, LOGIN {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Login.fxml";
        }
    }, KEY {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("addkey.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Key.fxml";
        }
    }, KEYS {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("keyview.title");
        }

		@Override
		public String getFxmlFile() {
			return "/view/Keys.fxml";
		}
    }, ACCOUNTS {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("accountview.title");
        }

		@Override
		public String getFxmlFile() {
			return "/view/Accounts.fxml";
		}
	}, SELECT_KEYS {

		@Override
		public String getTitle() {
			return "Select Keys";
		}

		@Override
		public String getFxmlFile() {
			return "/view/SelectKey.fxml";
		}
	}, SELECT_PHONES {

		@Override
		public String getTitle() {
			return "Select Phones";
		}

		@Override
		public String getFxmlFile() {
			return "/view/SelectKey.fxml";
		}
	}, LOGS {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("logsview.title");
		}

		@Override
		public String getFxmlFile() {
			return "/view/Logs.fxml";
		}
	}, EXPORT_LOGS {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("logexportview.title");
		}

		@Override
		public String getFxmlFile() {
			return "/view/LogExport.fxml";
		}		
	};
    
	public abstract String getTitle();
    public abstract String getFxmlFile();
    
    String getStringFromResourceBundle(String key){
        return ResourceBundle.getBundle("bundle").getString(key);
    }

}
