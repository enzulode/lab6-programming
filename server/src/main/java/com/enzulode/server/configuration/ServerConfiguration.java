package com.enzulode.server.configuration;

import com.enzulode.common.filesystem.FileManipulationService;
import com.enzulode.common.resolution.ResolutionServiceImpl;
import com.enzulode.models.util.LocalDateTimeAdapter;
import com.enzulode.network.UDPSocketServer;
import com.enzulode.network.exception.NetworkException;
import com.enzulode.server.dao.TicketDaoImpl;
import com.enzulode.server.database.TicketDatabaseImpl;
import com.enzulode.server.execution.ExecutionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

@Configuration
@Slf4j
public class ServerConfiguration
{
	@Bean(name = "fileManipulationService")
	public FileManipulationService fileManipulationServiceBean()
	{
		return new FileManipulationService();
	}

	@Bean(name = "localDateTimeAdapter")
	public LocalDateTimeAdapter localDateTimeAdapterBean()
	{
		return new LocalDateTimeAdapter();
	}

	@Bean(name = "jaxbContext")
	public JAXBContext jaxbContextBean()
	{
		try
		{
			return JAXBContext.newInstance(TicketDatabaseImpl.class);
		}
		catch (JAXBException e)
		{
			log.error("Failed to create jaxb context");
			throw new BeanCreationException("jaxbContext", "Failed to create jaxb context", e);
		}
	}

	@Bean(name = "jaxbMarshaller")
	public Marshaller jaxbMarshallerBean(JAXBContext context, LocalDateTimeAdapter adapter)
	{
		try
		{
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.setAdapter(adapter);
			return context.createMarshaller();
		}
		catch (JAXBException e)
		{
			log.error("Failed to create jaxb marshaller");
			throw new BeanCreationException("jaxbMarshaller", "Failed to create jaxb marshaller", e);
		}
	}

	@Bean(name = "jaxbUnmarshaller")
	public Unmarshaller jaxbUnmarshallerBean(JAXBContext context, LocalDateTimeAdapter adapter)
	{
		try
		{
			Unmarshaller unmarshaller = context.createUnmarshaller();
			unmarshaller.setAdapter(adapter);
			return unmarshaller;
		}
		catch (JAXBException e)
		{
			log.error("Failed to create jaxb unmarshaller");
			throw new BeanCreationException("jaxbUnmarshaller", "Failed to create jaxb unmarshaller", e);
		}
	}

	@Bean(name = "commandResolutionService")
	public ResolutionServiceImpl commandResolutionServiceBean()
	{
		return new ResolutionServiceImpl();
	}

	@Bean(name = "udpSocketServer")
	public UDPSocketServer udpServerBean()
	{
		try
		{
			return new UDPSocketServer();
		}
		catch (NetworkException e)
		{
			log.error("Failed to init socket server");
			throw new BeanCreationException("udpSocketServer", "Failed to init socket server", e);
		}
	}

	@Bean(name = "executionServiceImpl")
	public ExecutionServiceImpl executionServiceImplBean(TicketDaoImpl dao, FileManipulationService fms)
	{
		return new ExecutionServiceImpl(dao, fms);
	}
}
