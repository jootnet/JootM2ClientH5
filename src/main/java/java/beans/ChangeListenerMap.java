package java.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

abstract class ChangeListenerMap {
    private Map<String, PropertyChangeListener[]> map;

    protected abstract PropertyChangeListener[] newArray(int length);

    protected abstract PropertyChangeListener newProxy(String name, PropertyChangeListener listener);

    public final synchronized void add(String name, PropertyChangeListener listener) {
        if (this.map == null) {
            this.map = new HashMap<>();
        }
        PropertyChangeListener[] array = this.map.get(name);
        int size = (array != null)
                ? array.length
                : 0;

        PropertyChangeListener[] clone = newArray(size + 1);
        clone[size] = listener;
        if (array != null) {
            System.arraycopy(array, 0, clone, 0, size);
        }
        this.map.put(name, clone);
    }

    public final synchronized void remove(String name, PropertyChangeListener listener) {
        if (this.map != null) {
        	PropertyChangeListener[] array = this.map.get(name);
            if (array != null) {
                for (int i = 0; i < array.length; i++) {
                    if (listener.equals(array[i])) {
                        int size = array.length - 1;
                        if (size > 0) {
                        	PropertyChangeListener[] clone = newArray(size);
                            System.arraycopy(array, 0, clone, 0, i);
                            System.arraycopy(array, i + 1, clone, i, size - i);
                            this.map.put(name, clone);
                        }
                        else {
                            this.map.remove(name);
                            if (this.map.isEmpty()) {
                                this.map = null;
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public final synchronized PropertyChangeListener[] get(String name) {
        return (this.map != null)
                ? this.map.get(name)
                : null;
    }

    public final void set(String name, PropertyChangeListener[] listeners) {
        if (listeners != null) {
            if (this.map == null) {
                this.map = new HashMap<>();
            }
            this.map.put(name, listeners);
        }
        else if (this.map != null) {
            this.map.remove(name);
            if (this.map.isEmpty()) {
                this.map = null;
            }
        }
    }

    public final synchronized PropertyChangeListener[] getListeners() {
        if (this.map == null) {
            return newArray(0);
        }
        List<PropertyChangeListener> list = new ArrayList<>();

        PropertyChangeListener[] listeners = this.map.get(null);
        if (listeners != null) {
            for (PropertyChangeListener listener : listeners) {
                list.add(listener);
            }
        }
        for (Entry<String, PropertyChangeListener[]> entry : this.map.entrySet()) {
            String name = entry.getKey();
            if (name != null) {
                for (PropertyChangeListener listener : entry.getValue()) {
                    list.add(newProxy(name, listener));
                }
            }
        }
        return list.toArray(newArray(list.size()));
    }

    public final PropertyChangeListener[] getListeners(String name) {
        if (name != null) {
        	PropertyChangeListener[] listeners = get(name);
            if (listeners != null) {
            	PropertyChangeListener[] rets = new PropertyChangeListener[listeners.length];
            	for (int i = 0; i < listeners.length; ++i) {
            		rets[i] = listeners[i];
            	}
                return rets;
            }
        }
        return newArray(0);
    }

    public final synchronized boolean hasListeners(String name) {
        if (this.map == null) {
            return false;
        }
        PropertyChangeListener[] array = this.map.get(null);
        return (array != null) || ((name != null) && (null != this.map.get(name)));
    }

    public final Set<Entry<String, PropertyChangeListener[]>> getEntries() {
        return (this.map != null)
                ? this.map.entrySet()
                : Collections.<Entry<String, PropertyChangeListener[]>>emptySet();
    }

    public abstract PropertyChangeListener extract(PropertyChangeListener listener);
}
