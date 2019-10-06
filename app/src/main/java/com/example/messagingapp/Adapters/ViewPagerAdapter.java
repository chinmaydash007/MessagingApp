package com.example.messagingapp.Adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.messagingapp.Fragments.ChatFragment;
import com.example.messagingapp.Fragments.ContactsFragment;
import com.example.messagingapp.Fragments.GroupsFragment;
import com.example.messagingapp.Fragments.RequestFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ChatFragment();
            case 1:
                return new GroupsFragment();
            case 2:
                return new ContactsFragment();
            case 3:
                return new RequestFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Chats";
            case 1:
                return "Groups";
            case 2:
                return "Contacts";
            case 3:
                return "Requests";
            default:
                return "null";
        }

    }


}
