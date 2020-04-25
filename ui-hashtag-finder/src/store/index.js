import { createStore, applyMiddleware } from "redux";
import reducers from "./reducers";
import thunk from "redux-thunk";

export default () => {
    // return applyMiddleware(thunk)(createStore)(reducers);
    return createStore(reducers, applyMiddleware(thunk));
}