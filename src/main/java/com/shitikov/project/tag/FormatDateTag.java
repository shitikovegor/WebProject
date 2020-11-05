package com.shitikov.project.tag;

import com.shitikov.project.tag.util.DateFormatUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

@SuppressWarnings("serial")
public class FormatDateTag extends TagSupport {
    private static final Logger logger = LogManager.getLogger();
    private long date;
    private boolean input = false;
    public void setDate(long date) {
        this.date = date;
    }
    public void setInput(boolean input) {
        this.input = input;
    }

    @Override
    public int doStartTag() throws JspException {

        try {
            String dateFormatted;
            dateFormatted = DateFormatUtil.formatDate(date, input);
            pageContext.getOut().write(dateFormatted);

        } catch (IOException e) {
            logger.log(Level.WARN, "Error while writing data to jsp.");
            throw new JspException(e);
        }

        return SKIP_BODY;
    }
}
