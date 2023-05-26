package com.enzulode.common.network.request;

import com.enzulode.network.model.interconnection.Request;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class IdRequest extends Request
{
	private final int id;
}
