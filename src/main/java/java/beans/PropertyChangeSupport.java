package java.beans;

public class PropertyChangeSupport {
    private PropertyChangeListenerMap map = new PropertyChangeListenerMap();

    public PropertyChangeSupport(Object sourceBean) {
        if (sourceBean == null) {
            throw new NullPointerException();
        }
        source = sourceBean;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (listener instanceof PropertyChangeListenerProxy) {
            PropertyChangeListenerProxy proxy =
                   (PropertyChangeListenerProxy)listener;
            // Call two argument add method.
            addPropertyChangeListener(proxy.getPropertyName(),
                                      proxy.getListener());
        } else {
            this.map.add(null, listener);
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (listener instanceof PropertyChangeListenerProxy) {
            PropertyChangeListenerProxy proxy =
                    (PropertyChangeListenerProxy)listener;
            // Call two argument remove method.
            removePropertyChangeListener(proxy.getPropertyName(),
                                         proxy.getListener());
        } else {
            this.map.remove(null, listener);
        }
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return this.map.getListeners();
    }

    public void addPropertyChangeListener(
                String propertyName,
                PropertyChangeListener listener) {
        if (listener == null || propertyName == null) {
            return;
        }
        listener = this.map.extract(listener);
        if (listener != null) {
            this.map.add(propertyName, listener);
        }
    }

    public void removePropertyChangeListener(
                String propertyName,
                PropertyChangeListener listener) {
        if (listener == null || propertyName == null) {
            return;
        }
        listener = this.map.extract(listener);
        if (listener != null) {
            this.map.remove(propertyName, listener);
        }
    }

    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return this.map.getListeners(propertyName);
    }

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (oldValue == null || newValue == null || !oldValue.equals(newValue)) {
            firePropertyChange(new PropertyChangeEvent(this.source, propertyName, oldValue, newValue));
        }
    }

    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
        if (oldValue != newValue) {
            firePropertyChange(propertyName, Integer.valueOf(oldValue), Integer.valueOf(newValue));
        }
    }

    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        if (oldValue != newValue) {
            firePropertyChange(propertyName, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
        }
    }

    public void firePropertyChange(PropertyChangeEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        if (oldValue == null || newValue == null || !oldValue.equals(newValue)) {
            String name = event.getPropertyName();

            PropertyChangeListener[] common = this.map.get(null);
            PropertyChangeListener[] named = (name != null)
                        ? this.map.get(name)
                        : null;

            fire(common, event);
            fire(named, event);
        }
    }

    private static void fire(PropertyChangeListener[] listeners, PropertyChangeEvent event) {
        if (listeners != null) {
            for (PropertyChangeListener listener : listeners) {
                listener.propertyChange(event);
            }
        }
    }

    public void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) {
        if (oldValue == null || newValue == null || !oldValue.equals(newValue)) {
            firePropertyChange(new IndexedPropertyChangeEvent(source, propertyName, oldValue, newValue, index));
        }
    }

    public void fireIndexedPropertyChange(String propertyName, int index, int oldValue, int newValue) {
        if (oldValue != newValue) {
            fireIndexedPropertyChange(propertyName, index, Integer.valueOf(oldValue), Integer.valueOf(newValue));
        }
    }

    public void fireIndexedPropertyChange(String propertyName, int index, boolean oldValue, boolean newValue) {
        if (oldValue != newValue) {
            fireIndexedPropertyChange(propertyName, index, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
        }
    }

    public boolean hasListeners(String propertyName) {
        return this.map.hasListeners(propertyName);
    }

    private Object source;

    private static final class PropertyChangeListenerMap extends ChangeListenerMap {
        private static final PropertyChangeListener[] EMPTY = {};

        protected PropertyChangeListener[] newArray(int length) {
            return (0 < length)
                    ? new PropertyChangeListener[length]
                    : EMPTY;
        }

        protected PropertyChangeListener newProxy(String name, PropertyChangeListener listener) {
            return new PropertyChangeListenerProxy(name, listener);
        }

        public PropertyChangeListener extract(PropertyChangeListener listener) {
            while (listener instanceof PropertyChangeListenerProxy) {
                listener = ((PropertyChangeListenerProxy) listener).getListener();
            }
            return listener;
        }
    }
}
