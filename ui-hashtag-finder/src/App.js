import React from "react";
import { Grommet, Box } from "grommet";
import { grommet } from "grommet/themes";
import AppHeader from "./components/ui/AppHeader";
import SearchForm from "./components/containers/SearchForm";
import RelevantHashtags from "./components/containers/RelevantHashtags";
import { Provider } from "react-redux";
import storeFactory from "./store";

const store = storeFactory();

function App() {
  return (
    <>
      <Provider store={store}>
        <Grommet full theme={grommet}>
          <Box align="center" direction="column" gap="large">
            <AppHeader />
            <SearchForm />
            <RelevantHashtags />
          </Box>
        </Grommet>
      </Provider>
    </>
  );
}

export default App;
