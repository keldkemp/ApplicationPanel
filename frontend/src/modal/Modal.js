import React, { useState } from 'react';
import {Button, Modal, ModalHeader, ModalBody, ModalFooter, Fade} from 'reactstrap';
import * as PropTypes from "prop-types";

const ModalExample = (props) => {
    const {
        buttonLabel,
        className,
        isOpen,
        title,
        text,
        toggle
    } = props;

    return (
        <div>
            <Modal isOpen={isOpen} toggle={toggle} className={className}>
                <ModalHeader toggle={toggle}>{title}</ModalHeader>
                <ModalBody>
                    {text}
                </ModalBody>
                <ModalFooter>
                    <Button color="primary" onClick={toggle}>Ок</Button>{' '}
                    <Button color="secondary" onClick={toggle}>Отмена</Button>
                </ModalFooter>
            </Modal>
        </div>
    );
}

Modal.propTypes = {
    // boolean to control the state of the popover
    isOpen:  PropTypes.bool,
    autoFocus: PropTypes.bool,
    // if modal should be centered vertically in viewport
    centered: PropTypes.bool,
    // corresponds to bootstrap's modal sizes, ie. 'lg' or 'sm'
    size: PropTypes.string,
    // callback for toggling isOpen in the controlling component
    toggle:  PropTypes.func,
    role: PropTypes.string, // defaults to "dialog"
    // used to reference the ID of the title element in the modal
    labelledBy: PropTypes.string,
    keyboard: PropTypes.bool,
    // control backdrop, see http://v4-alpha.getbootstrap.com/components/modal/#options
    backdrop: PropTypes.oneOfType([
        PropTypes.bool,
        PropTypes.oneOf(['static'])
    ]),
    // if body of modal should be scrollable when content is long
    scrollable: PropTypes.bool,
    // allows for a node/component to exist next to the modal (outside of it). Useful for external close buttons
    external: PropTypes.node,
    // called on componentDidMount
    onEnter: PropTypes.func,
    // called on componentWillUnmount
    onExit: PropTypes.func,
    // called when done transitioning in
    onOpened: PropTypes.func,
    // called when done transitioning out
    onClosed: PropTypes.func,
    className: PropTypes.string,
    wrapClassName: PropTypes.string,
    modalClassName: PropTypes.string,
    backdropClassName: PropTypes.string,
    contentClassName: PropTypes.string,
    // boolean to control whether the fade transition occurs (default: true)
    fade: PropTypes.bool,
    cssModule: PropTypes.object,
    // zIndex defaults to 1000.
    zIndex: PropTypes.oneOfType([
        PropTypes.number,
        PropTypes.string,
    ]),
    // backdropTransition - controls backdrop transition
    // timeout is 150ms by default to match bootstrap
    // see Fade for more details
    backdropTransition: PropTypes.shape(Fade.propTypes),
    // modalTransition - controls modal transition
    // timeout is 300ms by default to match bootstrap
    // see Fade for more details
    modalTransition: PropTypes.shape(Fade.propTypes),
    innerRef: PropTypes.object,
    // if modal should be destructed/removed from DOM after closing
    unmountOnClose: PropTypes.bool, // defaults to true
    // if the element which triggered the modal to open should focused after the modal closes (see example somewhere below)
    returnFocusAfterClose: PropTypes.bool, // defaults to true
    // container to append the modal to
    container: PropTypes.oneOfType([PropTypes.string, PropTypes.func]),
    trapFocus: PropTypes.bool // Traps focus within modal
}

export default ModalExample;