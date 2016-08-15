package com.kk.lp.inter;

/**
 * Created by LinkedME06 on 16/7/11.
 */
public class InterfaceInstance {

    void getMessage(Callback callback, String message){
        callback.print(message);
    }

}
