import { useOutletContext } from 'react-router'
import { useForm } from 'react-hook-form';
import FormInput from '../components/TextInput';
import useShareJobMutation from '../query/queryHooks/useShareJobMutation';
import { useNavigate } from 'react-router';
import Modal from '../components/Modal';
import { useEffect } from 'react';
import Loading from '../components/Loading';

const ShareJob = () => {
    const { jobOpening }: any = useOutletContext();
    const navigate = useNavigate();
    const shareJobMutation = useShareJobMutation();
    
    const {
        register,
        handleSubmit,
        formState: { errors }
    } = useForm();

    const onSubmit = (data: any) => {
        shareJobMutation.mutate({ jobId: jobOpening.id, data });
    };

    useEffect(() => {
        if (shareJobMutation.isSuccess) {
            navigate(`/job-openings/${jobOpening.id}`);
        }
    }, [shareJobMutation.isSuccess, navigate, jobOpening.id]);

    const onCloseModal = () => {
        navigate(`/job-openings/${jobOpening.id}`);
    };

    return (
        <Modal
            heading={`Share Job: ${jobOpening.title}`}
            onSubmit={handleSubmit(onSubmit)}
            onClose={onCloseModal}
            submitButtonText={shareJobMutation.isPending ? "Sharing..." : "Share Job"}
            disabled={shareJobMutation.isPending}
        >
            <FormInput
                type="email"
                label="Recipient Email"
                id="email"
                placeholder="Enter email address"
                register={register}
                errors={errors}
                validation={{ 
                    required: 'Email is required',
                    pattern: {
                        value: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
                        message: 'Invalid email address'
                    }
                }}
                disabled={shareJobMutation.isPending}
            />
        </Modal>
    )
}

export default ShareJob
