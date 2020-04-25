import React, { useState } from "react"
import PropTypes from "prop-types";
import { TextInput, Box, Keyboard, Main, Button } from "grommet";
import { FormSearch } from "grommet-icons";
import Keyword from "../containers/Keyword";

const SearchForm = ({keywords = [], addKeywords = f => f, fetchHashtag = f => f}) => {
    const[currKeyword, setCurrKeyword] = useState("");

    const updateCurrKeyword = (newWord) => {
        if(newWord)
            setCurrKeyword(newWord.trim());
    }

    const onAddKeyword = () => {
        const regex = RegExp("[a-zA-Z0-9]+");
        if (regex.test(currKeyword)) {
            if (!keywords.includes(currKeyword)) {
                addKeywords(currKeyword);
            }
        }
        setCurrKeyword("");
    }

    return (
      <>
        <Main fill="horizontal">
          <Box fill="horizontal" alignSelf="center" align="center">
            <Box width="xlarge" gap="small" direction="row">
              <Keyboard onEnter={onAddKeyword} onSpace={onAddKeyword}>
                <TextInput
                  name = "textInput"
                  size="xlarge"
                  reverse
                  placeholder="Enter search word"
                  border
                  value={currKeyword}
                  onChange={(event) => updateCurrKeyword(event.target.value)}
                />
              </Keyboard>
              <Button
                icon={<FormSearch size="medium" color="brand" />}
                label="Submit"
                type="submit"
                onClick={() => fetchHashtag(keywords)}
              />
            </Box>
          </Box>
          <Box pad="xxsmall" gap="xxsmall" direction="row" wrap>
            {keywords.map((word, i) => (
              <Keyword key={i} keyword={word} />
            ))}
          </Box>
        </Main>
      </>
    );
}

SearchForm.propTypes = {
  keywords: PropTypes.array,
  addKeywords: PropTypes.func,
  fetchHashtags: PropTypes.func
}

export default SearchForm;