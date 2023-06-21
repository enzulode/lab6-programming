package com.enzulode.network.concurrent.structures;

import com.enzulode.network.model.transport.Frame;
import lombok.NonNull;

import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This map is designed for frame receiving
 *
 */
public class ConcurrentFrameReceivingMap
{
	/**
	 * Concurrent map instance
	 *
	 */
	private final ConcurrentMap<SocketAddress, List<Frame>> framesByAddressMap;

	/**
	 * Lock instance
	 *
	 */
	private final Lock lock;

	/**
	 * ConcurrentFrameReceivingMap constructor
	 *
	 */
	public ConcurrentFrameReceivingMap()
	{
		framesByAddressMap = new ConcurrentHashMap<>();
		lock = new ReentrantLock();
	}

	/**
	 * This method puts a new udp frame into the map using sender {@link SocketAddress} as key
	 *
	 * @param address sender address
	 * @param frame frame to be added into the map
	 */
	public void add(@NonNull SocketAddress address, @NonNull Frame frame)
	{
		if (framesByAddressMap.containsKey(address) && framesByAddressMap.get(address) != null)
		{
			framesByAddressMap.get(address).add(frame);
			return;
		}

		List<Frame> frames = new ArrayList<>();
		frames.add(frame);

		lock.lock();
		framesByAddressMap.put(address, frames);
		lock.unlock();
	}

	/**
	 * This method finds a list of udp frames for a specific socket address
	 *
	 * @param address requested frames address
	 * @return unmodifiable list of udp frames
	 */
	public List<Frame> findFramesByAddress(SocketAddress address)
	{
		return (framesByAddressMap.containsKey(address) && framesByAddressMap.get(address) != null)
				? Collections.unmodifiableList(framesByAddressMap.get(address))
				: Collections.emptyList();
	}

	/**
	 * This method returns a list of {@link Pair}. Each pair contains a frames sender address and
	 * the list of frame referring to it
	 *
	 * @return a list of pairs of socket address and list frames referring to this specific address
	 */
	public List<Pair<SocketAddress, List<Frame>>> findCompletedRequestsFrameLists()
	{
		List<Pair<SocketAddress, List<Frame>>> completedRequestsFramesList = new ArrayList<>();

		for (Iterator<Map.Entry<SocketAddress, List<Frame>>> i = framesByAddressMap.entrySet().iterator(); i.hasNext();)
		{
			Map.Entry<SocketAddress, List<Frame>> entry = i.next();

			if (validateFrameListCompleted(entry.getValue()))
			{
				lock.lock();
				completedRequestsFramesList.add(new Pair<>(entry.getKey(), entry.getValue()));
				i.remove();
				lock.unlock();
			}
		}

		return Collections.unmodifiableList(completedRequestsFramesList);
	}

	/**
	 * This method validates a list of frames for completion
	 *
	 * @param frames list of frames to be validated
	 * @return validation result. True if the last frame of the request contains frame that is marked as last
	 * and false otherwise
	 */
	private boolean validateFrameListCompleted(List<Frame> frames)
	{
		return frames.get(frames.size() - 1).last();
	}
}
