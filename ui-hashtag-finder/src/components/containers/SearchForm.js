import SearchForm from "../ui/SearchForm";
import { connect } from "react-redux";
import { addKeyword, fetchHashtags } from "../../actions"

const mapStateToProps = (state) => ({
    keywords: state.keywords
})

const mapDispatchToProps = dispatch => ({
    addKeywords: (keyword) => {
        dispatch(addKeyword(keyword));
    },
    fetchHashtag: (keywords) => {
        if(keywords)
            dispatch(fetchHashtags(keywords));
    }
})

const Container = connect(mapStateToProps, mapDispatchToProps)(SearchForm);

export default Container;