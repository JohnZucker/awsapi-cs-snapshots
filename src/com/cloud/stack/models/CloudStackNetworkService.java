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

package com.cloud.stack.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * @author slriv
 *
 */
public class CloudStackNetworkService {
	
	@SerializedName(ApiConstants.NAME)
	private String name;

	// this is annoying, why is this stored as a list of objects with a name/value  
	// the json looks like this
	// "capability" : [ { "name" : "val"},{ "name" : "val" }, ],
	// would be better if it were presented as:
	// "capability" : { "name" : "val", "name" : "val", "name" : "val" },
	@SerializedName(ApiConstants.CAPABILITY)
	private List<CloudStackNetworkServiceCapability> capabilities;

	/**
	 * 
	 */
	public CloudStackNetworkService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the capabilities
	 */
	public List<CloudStackNetworkServiceCapability> getCapabilities() {
		return capabilities;
	}
}
