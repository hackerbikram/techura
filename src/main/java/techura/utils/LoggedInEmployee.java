package techura.utils;

import techura.models.Employe;

public class LoggedInEmployee {
    private static Employe current;
    private static boolean islogin;

    public static void setCurrent(Employe emp) {
        current = emp;
    }

    public static Employe getCurrent() {
        return current;
    }
    public static boolean isIslogin() {
        Employe employe = getCurrent();
        if (employe == null) {
            setCurrent(null);
            return false; // Not logged in if current employee is null
        } else {
            return islogin; // Return the actual login status
        }
    }
}