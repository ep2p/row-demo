package com.example.demo.config;

import lab.idioglossia.row.annotations.Filter;
import lab.idioglossia.row.context.RowContextHolder;
import lab.idioglossia.row.context.RowUser;
import lab.idioglossia.row.domain.RowWebsocketSession;
import lab.idioglossia.row.domain.protocol.RequestDto;
import lab.idioglossia.row.domain.protocol.ResponseDto;
import lab.idioglossia.row.filter.RowFilter;
import lab.idioglossia.row.repository.RowSessionRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

@Filter(type = Filter.Type.BEFORE)
public class SecurityBasedRowFilter implements RowFilter {
    private final RowSessionRegistry sessionRegistry;

    public SecurityBasedRowFilter(RowSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public boolean filter(RequestDto requestDto, ResponseDto responseDto, RowWebsocketSession webSocketSession) throws Exception {
        RowUser rowUser = RowContextHolder.getContext().getRowUser();
        Assert.notNull(rowUser.getUserId(), "Context user id can not be null");
        RowWebsocketSession session = sessionRegistry.getSession(rowUser.getUserId(), rowUser.getSessionId());
        Assert.notNull(session, "Session can not be null");
        Authentication authentication = (Authentication) session.getExtra();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return true;
    }
}
