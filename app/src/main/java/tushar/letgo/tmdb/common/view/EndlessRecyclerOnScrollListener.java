package tushar.letgo.tmdb.common.view;

import android.support.v7.widget.RecyclerView;

import org.parceler.Parcel;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private State state;

    private int tempDy = 0;

    private RecyclerViewPositionHelper recyclerViewPositionHelper;

    public EndlessRecyclerOnScrollListener(RecyclerView recyclerView) {
        recyclerViewPositionHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (tempDy != 0 && dy != 0) {
            if (tempDy != dy) {
                onScroll();
            }
        }
        int totalItemCount = recyclerViewPositionHelper.getItemCount();
        int firstVisibleItem = recyclerViewPositionHelper.findFirstVisibleItemPosition();
        int visibleItemCount = totalItemCount - recyclerViewPositionHelper.findLastVisibleItemPosition();

        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (totalItemCount > 1) {
            int lastItemType = adapter.getItemViewType(totalItemCount - 1);
            int oneBeforeLastItemType = adapter.getItemViewType(totalItemCount - 2);

            if (lastItemType != oneBeforeLastItemType) {
                totalItemCount--;
            }
        } else if (totalItemCount == 1) {
            if (adapter.getItemViewType(0) == 1) {
                totalItemCount--;
            }
        }

        if (totalItemCount < state.previousTotalItemCount) {
            state.currentPage = state.startingPageIndex;
            state.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                state.loading = true;
            }
        }
        // If it's still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (state.loading && (totalItemCount > state.previousTotalItemCount)) {
            state.loading = false;
            state.previousTotalItemCount = totalItemCount;
            state.currentPage++;
        }

        // If it isn't currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!state.loading && (firstVisibleItem + visibleItemCount + state.visibleThreshold) >= totalItemCount) {
            state.loading = onLoadMore(state.currentPage + 1, totalItemCount);
        }

        if (recyclerViewPositionHelper.findFirstCompletelyVisibleItemPosition() == 0) {
            enableRefresh();
        } else {
            disableRefresh();
        }

        tempDy = dy;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void reset() {
        state.previousTotalItemCount = 0;
        state.currentPage = 0;
        state.startingPageIndex = 0;
        state.loading = true;
    }

    public abstract boolean onLoadMore(int page, int totalItemsCount);

    public abstract void enableRefresh();

    public abstract void disableRefresh();

    public abstract void onScroll();

    @Parcel
    public static class State {
        // The minimum number of items to have below your current scroll position
        // before loading more.
        int visibleThreshold = 5;
        // The current offset index of data you have loaded
        int currentPage = 0;
        // The total number of items in the dataset after the last load
        int previousTotalItemCount = 0;
        // True if we are still waiting for the last set of data to load.
        boolean loading = true;
        // Sets the starting page index
        int startingPageIndex = 0;
    }
}
