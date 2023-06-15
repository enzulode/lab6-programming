package com.enzulode.server.handling;

import com.enzulode.common.dao.TicketDao;
import com.enzulode.common.execution.ExecutionService;
import com.enzulode.common.network.request.CommandRequest;
import com.enzulode.common.network.request.IdRequest;
import com.enzulode.common.network.response.CommandResponse;
import com.enzulode.common.network.response.IdResponse;
import com.enzulode.common.resolution.ResolutionService;
import com.enzulode.models.Ticket;
import com.enzulode.network.handling.RequestHandler;
import com.enzulode.network.model.interconnection.Request;
import com.enzulode.network.model.interconnection.Response;
import com.enzulode.network.model.interconnection.util.ResponseCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Default application requests handler implementation
 *
 */
@RequiredArgsConstructor
@Slf4j
public class DefaultApplicationRequestHandlerImpl implements RequestHandler
{
	/**
	 * Ticket dao instance
	 *
	 */
	private final TicketDao dao;

	/**
	 * Command resolution service
	 *
	 */
	private final ResolutionService resolutionService;

	/**
	 * Command execution service
	 *
	 */
	private final ExecutionService<Ticket> executionService;

	/**
	 * Handling method
	 *
	 * @param request request that is being handled
	 * @return response instance
	 */
	@Override
	public Response handle(@NonNull Request request)
	{
		if (request instanceof CommandRequest req)
		{
			req.getCommand().setResolutionService(resolutionService);
			req.getCommand().setExecutionService(executionService);

			log.info("Proceeding new request: " + req.getClass().getSimpleName());
			return new CommandResponse(
					ResponseCode.SUCCEED,
					executionService.execute(req.getCommand())
			);
		}

		if (request instanceof IdRequest req)
		{
			ResponseCode code = (dao.checkIdExistence(req.getId())) ? ResponseCode.SUCCEED : ResponseCode.FAILED;

			if (code == ResponseCode.FAILED)
				return new IdResponse(code, "No such id found");
			else
				return new IdResponse(code, "Id was found");
		}

		return null;
	}
}
