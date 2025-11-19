package interface_adapter.studyset.studyset_browse;

import interface_adapter.ViewModel;

public class BrowseStudySetViewModel extends ViewModel<BrowseStudySetState> {
    public BrowseStudySetViewModel() {
        super("browse study set");
        setState(new BrowseStudySetState());
    }
}
