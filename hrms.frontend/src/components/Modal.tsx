import type { ReactNode } from 'react';
import { XMarkIcon } from '@heroicons/react/24/solid';

interface ModalProps {
    heading: string;
    children: ReactNode;
    onSubmit: () => void;
    onClose: () => void;
    submitButtonText: string;
    disabled?: boolean;
}

const Modal = ({ heading, children, onSubmit, onClose, submitButtonText, disabled }: ModalProps) => {
    return (
        <div
            id="default-modal"
            tabIndex={1}
            aria-hidden="true"
            className="bg-transparent backdrop-blur overflow-y-auto overflow-x-hidden fixed top-0 right-0 left-0 z-50 flex items-center justify-center w-full md:inset-0 max-h-full p-4"
        >
            <div className="relative w-full max-w-2xl max-h-full bg-white rounded">
                <div className="relative rounded bg-neutral-primary-soft border border-default shadow-2xl rounded-base p-4 md:p-6">
                    <div className="flex items-center justify-between border-b border-default pb-4 md:pb-5">
                        <h3 className="text-lg font-medium text-heading">{heading}</h3>
                        <button
                            type="button"
                            className="text-body bg-transparent hover:bg-neutral-tertiary hover:text-heading rounded-base text-sm w-9 h-9 ms-auto cursor-pointer inline-flex justify-center items-center"
                            data-modal-hide="default-modal"
                            onClick={onClose}
                        >
                            <XMarkIcon />
                        </button>
                    </div>
                    <div className="space-y-4 md:space-y-6 py-4 md:py-6">
                        {children}
                    </div>
                    <div className="flex items-center border-t border-default space-x-4 pt-4 md:pt-5">
                        <button
                            type="submit"
                            className={`text-white box-border border border-transparent shadow-xs font-medium leading-5 rounded-base text-sm px-4 py-2.5 focus:outline-none cursor-pointer rounded ${
                                disabled 
                                    ? 'bg-gray-400 cursor-not-allowed' 
                                    : 'bg-gray-800 hover:bg-brand-strong'
                            }`}
                            onClick={onSubmit}
                            disabled={disabled}
                        >
                            {submitButtonText}
                        </button>
                        <button
                            data-modal-hide="default-modal"
                            type="button"
                            className="text-body rounded bg-neutral-secondary-medium box-border border border-default-medium hover:bg-neutral-tertiary-medium hover:text-heading focus:ring-4 focus:ring-neutral-tertiary shadow-xs font-medium cursor-pointer leading-5 rounded-base text-sm px-4 py-2.5 focus:outline-none"
                            onClick={onClose}
                            disabled={disabled}
                        >
                            Cancel
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Modal
