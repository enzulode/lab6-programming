package com.enzulode.network.model.interconnection.response;

/**
 * This enumeration declares available application response codes
 *
 */
public enum ResponseCode
{
	/**
	 * If the request succeed
	 *
	 */
	SUCCEED,

	/**
	 * If the request failed
	 *
	 */
	FAILED,

	/**
	 * If the requester had not enough authorities to access the resource
	 *
	 */
	UNAUTHORISED;
}
