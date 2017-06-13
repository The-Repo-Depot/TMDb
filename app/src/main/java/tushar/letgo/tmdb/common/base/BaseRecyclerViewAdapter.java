package tushar.letgo.tmdb.common.base;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<T> objects;

    protected boolean notifyOnChange = true;

    protected final Object lock = new Object();

    public BaseRecyclerViewAdapter(List<T> objects) {
        this.objects = objects;
    }

    @Override
    public int getItemCount() {
        return objects != null ? objects.size() : 0;
    }

    public T getItem(int position) {
        if (objects == null) {
            return null;
        }

        return objects.get(position);
    }

    public List<T> getAllItems() {
        return objects;
    }

    public void add(T object) {
        synchronized (lock) {
            if (objects == null) {
                objects = new ArrayList<>();
            }

            objects.add(object);
        }

        if (notifyOnChange) {
            notifyItemInserted(objects.size() - 1);
        }
    }

    public void addAll(Collection<? extends T> collection) {
        if (collection != null) {
            synchronized (lock) {
                if (objects == null) {
                    objects = new ArrayList<>();
                }

                objects.addAll(collection);
            }

            if (notifyOnChange) {
                if (objects.size() - collection.size() != 0) {
                    notifyItemRangeChanged(objects.size() - collection.size(), collection.size());
                } else {
                    notifyDataSetChanged();
                }
            }
        }
    }

    public void addAll(T... objects) {
        synchronized (lock) {
            if (this.objects == null) {
                this.objects = new ArrayList<>();
            }

            Collections.addAll(this.objects, objects);
        }

        if (notifyOnChange) {
            if (this.objects.size() - objects.length != 0) {
                notifyItemRangeChanged(this.objects.size() - objects.length, objects.length);
            } else {
                notifyDataSetChanged();
            }
        }
    }

    public void insert(T object, int index) {
        synchronized (lock) {
            if (objects == null) {
                objects = new ArrayList<>();
            }

            objects.add(index, object);
        }

        if (notifyOnChange) {
            notifyItemInserted(index);
        }
    }

    public void remove(T object) {
        int removeIndex;

        synchronized (lock) {
            if (objects == null) {
                return;
            }

            removeIndex = objects.indexOf(object);

            if (removeIndex != -1) {
                objects.remove(removeIndex);
            }
        }

        if (notifyOnChange && removeIndex != -1) {
            notifyDataSetChanged();
        }
    }

    public void clear() {
        synchronized (lock) {
            if (objects == null) {
                return;
            }

            objects.clear();
        }

        if (notifyOnChange) {
            notifyDataSetChanged();
        }
    }

    public void sort(Comparator<? super T> comparator) {
        synchronized (lock) {
            if (objects == null) {
                return;
            }

            Collections.sort(objects, comparator);
        }

        if (notifyOnChange) {
            notifyDataSetChanged();
        }
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        this.notifyOnChange = notifyOnChange;
    }
}

