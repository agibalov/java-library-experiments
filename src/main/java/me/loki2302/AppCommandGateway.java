package me.loki2302;

import me.loki2302.domain.exceptions.BusinessException;

public interface AppCommandGateway {
    <R> R sendAndWait(Object command) throws BusinessException;
}
