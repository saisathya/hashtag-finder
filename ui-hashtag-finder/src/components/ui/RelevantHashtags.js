import React from "react";
import { Box, TextArea, Button } from "grommet";
import { Clipboard, FormRefresh } from "grommet-icons";
import PropTypes from "prop-types";
import ResultTable from "../ui/ResultTable";

const copy = () => {
  let ele = document.getElementById("suggestedHashtags");
  ele.select();
  document.execCommand("copy");
};

const RelevantHashtags = ({
  hashtags,
  recommendedTags,
  userSuggested,
  resetSuggestedTags,
}) => {
  return (
    <>
      <Box pad="xsmall" direction="column" flex="grow" align="center">
        {userSuggested.length > 0 && (
          <Box pad="xsmall" height="small" width="large" direction="row">
            <TextArea
              id="suggestedHashtags"
              value={userSuggested.join(" ")}
              readOnly
              fill={true}
              resize={false}
              size="medium"
            />
            <Box direction="column">
              <Button
                margin="xsmall"
                plain={false}
                icon={<Clipboard />}
                onClick={copy}
                label="copy"
              />
              <Button
                margin="xsmall"
                plain={false}
                icon={<FormRefresh />}
                onClick={() => resetSuggestedTags(recommendedTags)}
                label="reset"
              />
            </Box>
          </Box>
        )}
        <Box
          alignContent="start"
          align="start"
          overflow={{ vertical: "hidden", horizontal: "auto" }}
        >
          {hashtags.map((hashtag, i) => (
            <ResultTable
              key={i}
              keyword={hashtag.searchWord}
              hashtags={hashtag.hashtags}
            />
          ))}
        </Box>
      </Box>
    </>
  );
};

RelevantHashtags.propTypes = {
  hashtags: PropTypes.array,
  recommendedHastags: PropTypes.array,
  suggestedHashtags: PropTypes.array,
  resetSugestedTags: PropTypes.func,
};

export default RelevantHashtags;
