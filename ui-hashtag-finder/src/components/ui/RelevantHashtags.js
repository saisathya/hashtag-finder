import React from "react";
import { Box, TextArea, Button } from "grommet";
import { Clipboard, FormRefresh, ClearOption } from "grommet-icons";
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
        {recommendedTags.length > 0 && (
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
                icon={<Clipboard color="brand"/>}
                onClick={copy}
                label="copy"
              />
              <Button
                margin="xsmall"
                plain={false}
                icon={<ClearOption color="brand"/>}
                onClick={() => resetSuggestedTags([])}
                label="clear"
              />
              <Button
                margin="xsmall"
                plain={false}
                icon={<FormRefresh color="brand"/>}
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
