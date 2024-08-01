package org.kobic.bioexpress.rcp.gbox.handler;

import org.kobic.bioexpress.rcp.gbox.handler.GBoxTransferHandler.TransferFutureHandler;

import raonwiz.k.livepass.client.KLivepassClientEvent;
import raonwiz.k.livepass.client.KLivepassClientEventVo;
import raonwiz.k.livepass.client.event.KLivepassClientTransferFinishEventListener;
import raonwiz.k.livepass.client.event.KLivepassClientTransferProgressEventListener;
import raonwiz.k.livepass.client.event.KLivepassClientTransferStartEventListener;
import raonwiz.k.livepass.client.event.KLivepassClientTransferStopEventListener;

public class KLivepassEventHandler {

	public KLivepassClientEvent setHandler(TransferFutureHandler pHandler) {

		KLivepassClientEvent event = new KLivepassClientEvent();

		final TransferFutureHandler handler = pHandler;

		event.addKLivepassClientTransferStartEventListener(new KLivepassClientTransferStartEventListener() {
			public void kLivepassClientTransferStartEvent(KLivepassClientEventVo pEventVo) {
				handler.started(pEventVo);
			}
		});

		event.addKLivepassClientTransferProgressEventListener(new KLivepassClientTransferProgressEventListener() {
			public void kLivepassClientTransferProgressEvent(KLivepassClientEventVo pEventVo) {
				handler.progressed(pEventVo);
			}
		});

		event.addKLivepassClientTransferFinishEventListener(new KLivepassClientTransferFinishEventListener() {
			public void kLivepassClientTransferFinishEvent(KLivepassClientEventVo pEventVo) {
				handler.finished(pEventVo);
			}
		});

		event.addKLivepassClientTransferStopEventListener(new KLivepassClientTransferStopEventListener() {
			@Override
			public void kLivepassClientTransferStopEvent(KLivepassClientEventVo pEventVo) {
				handler.stop(pEventVo);

			}
		});

		return event;
	}

}
