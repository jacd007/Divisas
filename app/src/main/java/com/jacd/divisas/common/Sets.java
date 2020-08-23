package com.jacd.divisas.common;


public abstract class Sets {


    public static final String COUNTRY = "VE";
    public static final int USD_TO_COP = 3000;
    public static final int HTTP_TIME_OUT = 15000;


    public static abstract class Keys {
        public static final String SP_KEY = "SharedPreferences_key";
        public static final int ACTIVITY_REVIEW = 543;
        public static final int ACTIVITY_MAP = 5332;







        public static abstract class status {

            public static final int REQ_CODE_COMANDA = 2020;
            public static final int PEDIDO = 1;
            public static final int ACEPTADO_PARTNER = 2;
            public static final int ACEPTADO = 3;
            public static final int CERCA = 4;
            public static final int RECIBIDO = 5;
            public static final int LLEGANDO = 6;
            public static final int ENTREGADO = 7;
            public static final int CANCELADA_USUARIO = 8;
            public static final int CANCELADA_DELIVERY = 9;


        }

        /**
         * este es por requerimiento de backend y fue colocado despues
         */

        public enum OrderType {
            Llevame(1), LlevameMoto(2), Mandado(3), Pedido(4);
            private final int val;
            private OrderType(int v) { val = v; }
            public int getVal() { return val; }

        }




        public enum category{
            LLEVAME(1),RESTAURANTE(2),FARMACIA(3),SUPERMERCADO(4),LLEVAME_MOTO(5),DELIVERY(6);

            category(int i) {
            }


        }




        public static final String POSTCODE_PATTERN = "^(?:[WE]C|(?:E|SW)(?:[1-9]|1[0-9]|[12]0)|N(?:[1-9]|1[0-9]|2[0-2])|NW(?:[1-9]|1[01])|SE(?:[1-9]|1[0-9]|2[0-8])|W(?:[1-9]|1[0-4])|HA[0-9]|EN[1-8])$";

        public static abstract class SharedKeys {
            public static final String EMAIL_KEY = "current_email_key";
            public static final String FN_KEY = "firstname_current_key";
            public static final String SN_KEY = "surname_current_key";
            public static final String GROUP_KEY = "group_current_key";
            public static final String STUDENT_ID_KEY = "student_id_key";
            public static final String LOGGED_KEY = "logged_key";
            public static final String PRELOADBOOKS = "_preloadbooks";
            public static final String PRELOADCATEGORIES = "_preloadcategories";
            public static final String PRELOADUSERS = "_preloadusers";
        }

    }

}

