package com.od.mma.Membership;

/**
 * Created by awais on 10/01/2017.
 */

public interface FragInterface {
    public void NavigateTo(int position);
    public void onFragmentNav(MembershipToNav nav);

    public enum MembershipToNav {
            NEXT, BACK
    }
}
