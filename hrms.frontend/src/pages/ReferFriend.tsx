import { useOutletContext } from 'react-router'
import { useForm } from 'react-hook-form';
import FormInput from '../components/TextInput';
import useReferFriendMutation from '../query/queryHooks/useReferFriendMutation';
import { useNavigate } from 'react-router';
import Modal from '../components/Modal';
import { useEffect } from 'react';
import Loading from '../components/Loading';

const ReferFriend = () => {
    const { jobOpening } = useOutletContext() as { jobOpening: any };
    const navigate = useNavigate();
    const referFriendMutation = useReferFriendMutation();
    
    const {
        register,
        handleSubmit,
        formState: { errors }
    } = useForm();

    const onSubmit = (data: any) => {
        const formData = new FormData();
        formData.append('name', data.name);
        formData.append('email', data.email);
        if (data.shortNote) formData.append('shortNote', data.shortNote);
        if (data.cvMedia && data.cvMedia[0]) formData.append('cvMedia', data.cvMedia[0]);
        
        referFriendMutation.mutate({ jobId: jobOpening.id, data: formData });
    };

    useEffect(() => {
        if (referFriendMutation.isSuccess) {
            navigate(`/job-openings/${jobOpening.id}`);
        }
    }, [referFriendMutation.isSuccess, navigate, jobOpening.id]);

    const onCloseModal = () => {
        navigate(`/job-openings/${jobOpening.id}`);
    };

    return (
        <Modal
            heading={`Refer Friend for: ${jobOpening.title}`}
            onSubmit={handleSubmit(onSubmit)}
            onClose={onCloseModal}
            submitButtonText={referFriendMutation.isPending ? "Referring..." : "Refer Friend"}
            disabled={referFriendMutation.isPending}
        >
            <FormInput
                label="Friend Name"
                id="name"
                placeholder="Enter friend's name"
                register={register}
                errors={errors}
                validation={{ required: 'Name is required' }}
                disabled={referFriendMutation.isPending}
            />
            <FormInput
                type="email"
                label="Friend Email"
                id="email"
                placeholder="Enter friend's email"
                register={register}
                errors={errors}
                validation={{ 
                    required: 'Email is required',
                    pattern: {
                        value: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
                        message: 'Invalid email address'
                    }
                }}
                disabled={referFriendMutation.isPending}
            />
            <FormInput
                label="Short Note (Optional)"
                id="shortNote"
                placeholder="Add a short note about your friend"
                register={register}
                errors={errors}
                disabled={referFriendMutation.isPending}
            />
            <FormInput
                type="file"
                label="CV"
                id="cvMedia"
                validation={{
                    required: 'CV is required',
                }}
                placeholder=""
                register={register}
                errors={errors}
                disabled={referFriendMutation.isPending}
            />
        </Modal>
    )
}

export default ReferFriend
