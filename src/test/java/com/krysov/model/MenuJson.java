package com.krysov.model;

import java.util.List;

public class MenuJson {
    public boolean menu;
    public int id;
    public String value;
    public List<Menuitem> menuitem;

    public static class Menuitem {
        public String value;
        public String onclick;

    }
}
