import RelevantHashtags from "../ui/RelevantHashtags";
import { resetSuggestedTags } from "../../actions";
import { connect } from "react-redux";

const mapStateToProps = (state) => ({
    hashtags: state.hashtags,
    recommendedTags: state.suggestedTags.recommendedTags,
    userSuggested: state.suggestedTags.userSuggested
})

const mapDispatchToProps = dispatch => ({
    resetSuggestedTags: (hashtag) => {
        dispatch(resetSuggestedTags(hashtag));
    }
});

const Container = connect(mapStateToProps, mapDispatchToProps)(RelevantHashtags);

export default Container;