import Keyword from "../ui/Keyword";
import { connect } from "react-redux";
import { deleteKeyword } from "../../actions";

const mapStateToProps = (state) => ({
    
})

const mapDispatchToProps = dispatch => ({
    deleteKeywords: (keyword) => {
        dispatch(deleteKeyword(keyword));
    },
})

const Container = connect(mapStateToProps, mapDispatchToProps)(Keyword);

export default Container;