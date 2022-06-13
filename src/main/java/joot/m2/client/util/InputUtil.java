package joot.m2.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.google.gwt.dom.client.*;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类用于在Canvas元素上叠加<input>标签来解决无法输入中文的问题
 */
public final class InputUtil {
    private static Map<String, InputElement> inputs = new HashMap<>();

    public interface KeyUpEventListener {
        void onKeyUp(String key);
    }

    public interface TextFiledFilter {
        boolean accept(char c);
    }

    public interface FocusListener {
        void focusChange(boolean focused);
    }

    public static InputElement newInput(String id, int x, int y, int width, int tabIndex, String fontColor, String backgroundColor) {
        InputElement input = inputs.get(id);
        if (input == null) {
            input = Document.get().createTextInputElement();
            input.setClassName("jootm2");
            input.setTabIndex(tabIndex);
            input.getStyle().setColor(fontColor);
            input.getStyle().setBackgroundColor(backgroundColor);
            input.getStyle().setBorderStyle(Style.BorderStyle.NONE);
            input.getStyle().setOutlineStyle(Style.OutlineStyle.NONE);
            input.getStyle().setPosition(Style.Position.ABSOLUTE);
            input.getStyle().setLeft(((GwtApplication)Gdx.app).getRootPanel().getElement().getAbsoluteLeft() + x, Style.Unit.PX);
            input.getStyle().setTop(((GwtApplication)Gdx.app).getRootPanel().getElement().getAbsoluteTop() + y, Style.Unit.PX);
            input.getStyle().setWidth(width, Style.Unit.PX);
            input.getStyle().setFontSize(12, Style.Unit.PX);
            inputs.put(id, input);
            RootPanel.get().getElement().appendChild(input);
        }
        input.setDisabled(true);
        input.getStyle().setZIndex(-1);
        input.focus();
        calcVisible();
        return input;
    }

    public static InputElement newPassword(String id, int x, int y, int width, int tabIndex, String fontColor, String backgroundColor) {
        InputElement input = inputs.get(id);
        if (input == null) {
            input = Document.get().createPasswordInputElement();
            input.setClassName("jootm2");
            input.setTabIndex(tabIndex);
            input.getStyle().setColor(fontColor);
            input.getStyle().setBackgroundColor(backgroundColor);
            input.getStyle().setBorderStyle(Style.BorderStyle.NONE);
            input.getStyle().setOutlineStyle(Style.OutlineStyle.NONE);
            input.getStyle().setPosition(Style.Position.ABSOLUTE);
            input.getStyle().setLeft(((GwtApplication)Gdx.app).getRootPanel().getElement().getAbsoluteLeft() + x, Style.Unit.PX);
            input.getStyle().setTop(((GwtApplication)Gdx.app).getRootPanel().getElement().getAbsoluteTop() + y, Style.Unit.PX);
            input.getStyle().setWidth(width, Style.Unit.PX);
            input.getStyle().setFontSize(12, Style.Unit.PX);
            inputs.put(id, input);
            RootPanel.get().getElement().appendChild(input);
        }
        input.setDisabled(true);
        input.getStyle().setZIndex(-1);
        input.focus();
        calcVisible();
        return input;
    }

    public static native void setTextFieldFilter(InputElement ele, TextFiledFilter filter)/*-{
    ele.addEventListener('compositionstart', function (e) {
        e.target.composing = true;
    });
    ele.addEventListener('compositionend', function (e) {
        e.target.composing = false;
        e.target.dispatchEvent(new InputEvent('input'));
    });
    ele.addEventListener('input', function(e) {
        if (e.target.composing) return;
        var text_value = e.target.value;
        var filter_value = '';
        for (var i = 0; i < text_value.length; ++i) {
            var _char = text_value.charCodeAt(i);
            var accept = filter.@joot.m2.client.util.InputUtil.TextFiledFilter::accept(C)(_char);
            if (accept) {
                filter_value = filter_value.concat(text_value.charAt(i));
            }
        }
        e.target.value = filter_value;
    });
    }-*/;

    public static native void hookKeyUp(InputElement ele, KeyUpEventListener listener)/*-{
    ele.addEventListener('keyup', function(e) {
       listener.@joot.m2.client.util.InputUtil.KeyUpEventListener::onKeyUp(Ljava/lang/String;)(e.key);
    });
    }-*/;

    public static native void onFocusChange(InputElement ele, FocusListener listener)/*-{
    ele.addEventListener('blur', function(e) {
       listener.@joot.m2.client.util.InputUtil.FocusListener::focusChange(Z)(false);
    });
    ele.addEventListener('focus', function(e) {
        listener.@joot.m2.client.util.InputUtil.FocusListener::focusChange(Z)(true);
    });
    }-*/;

    public static void show(String id) {
        if (inputs.containsKey(id)) {
            inputs.get(id).getStyle().setZIndex(1000);
            inputs.get(id).setDisabled(false);
        }
        calcVisible();
    }

    public static void hide(String id) {
        if (inputs.containsKey(id)) {
            inputs.get(id).getStyle().setZIndex(-1);
            inputs.get(id).setDisabled(true);
            inputs.get(id).setValue("");
        }
        calcVisible();
    }

    public static void hideAll() {
        for (InputElement ele : inputs.values()) {
            ele.getStyle().setZIndex(-1);
            ele.setDisabled(true);
            ele.setValue("");
        }
        calcVisible();
    }

    private static void calcVisible() {
        int visibleCount = 0;
        for (InputElement ele : inputs.values()) {
            if (!ele.isDisabled()) visibleCount++;
        }
        if (visibleCount == 0) {
            Gdx.input.setCatchKey(Input.Keys.BACKSPACE, true);
            Gdx.input.setCatchKey(Input.Keys.TAB, true);
        } else {
            Gdx.input.setCatchKey(Input.Keys.BACKSPACE, false);
            Gdx.input.setCatchKey(Input.Keys.TAB, false);
        }
    }
}
