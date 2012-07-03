/*
 * Copyright (C) 2011 Citrix Systems, Inc.  All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloud.bridge.persist.dao;

import org.apache.log4j.Logger;

import com.cloud.bridge.persist.EntityDao;
import com.cloud.stack.models.CloudStackConfiguration;


public class CloudStackConfigurationDao extends EntityDao<CloudStackConfiguration> {
	public static final Logger logger = Logger.getLogger(CloudStackConfigurationDao.class);

	public CloudStackConfigurationDao() {
	    super(CloudStackConfiguration.class, true);
	}


	public String getConfigValue( String configName ){
		CloudStackConfiguration config = queryEntity("from CloudStackConfiguration where name=?", new Object[] {configName});
		if(config != null){
		    return config.getValue();
		}
		return null;
	}


}
