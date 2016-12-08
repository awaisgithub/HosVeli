package com.od.mma.event;

public interface EventListAdapterInterface {
    public void gotoDetailActivity(String id);
    public void performOperationOnEvent(EventOP op, String eventId);

    public enum EventOP {
        LAUNCH_ACTIVITY, SHARE_SOCIAL, MARK_FAV, UNMARK_FAV
    }
}
