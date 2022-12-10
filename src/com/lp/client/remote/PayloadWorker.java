package com.lp.client.remote;

import javax.swing.SwingWorker;

import com.lp.server.system.service.PayloadDto;

public abstract class PayloadWorker<T> extends SwingWorker<T, PayloadDto> implements IPayloadPublisher {

}
