package com.ludufre.plugins.screenshot;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.PixelCopy;
import android.view.View;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.io.ByteArrayOutputStream;

@CapacitorPlugin(name = "Screenshot")
public class ScreenshotPlugin extends Plugin {
    @PluginMethod
    public void take(PluginCall call) {
        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    Bitmap img = null;
                    View view = getBridge().getWebView();
                    view.setDrawingCacheEnabled(true);
                    img = Bitmap.createBitmap(view.getDrawingCache());
                    view.setDrawingCacheEnabled(false);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    JSObject ret = new JSObject();
                    ret.put("base64", new String(Base64.encode(byteArray, Base64.NO_WRAP)));
                    call.resolve(ret);
                }catch (Exception e){
                    call.reject("cap err",e);
                }
            }
        });
    }

    @PluginMethod
    public void takeO(PluginCall call){
        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    Bitmap img = null;
                    View webView = getBridge().getWebView();
                    int width = webView.getWidth();
                    int height = webView.getHeight();
                    img = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
                    Rect rect = new Rect();
                    webView.getGlobalVisibleRect(rect);
                    Bitmap finalImg = img;
                    PixelCopy.request(getBridge().getActivity().getWindow(), rect, img, new PixelCopy.OnPixelCopyFinishedListener() {
                        @Override
                        public void onPixelCopyFinished(int copyResult) {
                            if (copyResult == PixelCopy.SUCCESS){
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                finalImg.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();

                                JSObject ret = new JSObject();
                                ret.put("base64", new String(Base64.encode(byteArray, Base64.NO_WRAP)));
                                call.resolve(ret);
                            }
                        }
                    },new Handler(Looper.getMainLooper()));
                }catch (Exception e){
                    call.reject("cap err",e);
                }
            }
        });
    }
}
