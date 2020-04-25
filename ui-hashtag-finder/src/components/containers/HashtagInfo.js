import HashtagInfo from "../ui/HashtagInfo";
import { connect } from "react-redux";
import {
  addSuggestedTag,
  deleteSuggestedTag,
} from "../../actions";

const mapStateToProps = (state) => ({
  userSuggested: state.suggestedTags.userSuggested,
});

const mapDispatchToProps = (dispatch) => ({
  addSuggestedTag: (hashtag) => {
    dispatch(addSuggestedTag([hashtag]));
  },
  deleteSuggestedTag: (hashtag) => {
    dispatch(deleteSuggestedTag([hashtag]));
  },
});

const Container = connect(mapStateToProps, mapDispatchToProps)(HashtagInfo);

export default Container;
