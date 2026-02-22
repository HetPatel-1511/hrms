import { useEffect } from 'react'
import Button from '../components/Button'
import { useForm } from 'react-hook-form';
import FormInput from '../components/TextInput';
import useAddGameMutation from '../query/queryHooks/useAddGameMutation';
import { useNavigate } from 'react-router';

const AddGames = () => {
    const navigate = useNavigate();
    const {
        register,
        handleSubmit,
        formState: { errors }
    } = useForm();

    const addGame = useAddGameMutation()
    
    const onSubmit = (data: any) => {
        addGame.mutate(data);
    };

    useEffect(() => {
        if (addGame.isSuccess) {
            navigate('/games')
        }
    }, [addGame.isSuccess])

    return (
        <div>
            <h1 className='text-2xl mb-4'>Add Game</h1>
            <form className="">
                <div className="mb-5">
                    <FormInput
                        label="Game Name"
                        id="name"
                        placeholder="Enter game name"
                        register={register}
                        errors={errors}
                        validation={{ required: 'Game name is mandatory' }}
                    />
                </div>
                <div className="mb-5">
                    <FormInput
                        label="Slot Release Time"
                        id="slotReleaseTime"
                        type="time"
                        register={register}
                        errors={errors}
                        validation={{ required: 'Slot release time is mandatory' }}
                    />
                </div>
                <div className="mb-5">
                    <FormInput
                        label="Slot Duration (minutes)"
                        id="slotDurationMinutes"
                        type="number"
                        placeholder="Enter slot duration in minutes"
                        register={register}
                        errors={errors}
                        validation={{ required: 'Slot duration is mandatory', min: { value: 1, message: 'Duration must be at least 1 minute' } }}
                    />
                </div>
                <div className="mb-5">
                    <FormInput
                        label="Max Players Per Slot"
                        id="maxPlayersPerSlot"
                        type="number"
                        placeholder="Enter maximum players per slot"
                        register={register}
                        errors={errors}
                        validation={{ required: 'Max players per slot is mandatory', min: { value: 1, message: 'Must have at least 1 player' } }}
                    />
                </div>
                <div className="mb-5">
                    <FormInput
                        label="Operating Start Time"
                        id="operatingStartTime"
                        type="time"
                        register={register}
                        errors={errors}
                        validation={{ required: 'Operating start time is mandatory' }}
                    />
                </div>
                <div className="mb-5">
                    <FormInput
                        label="Operating End Time"
                        id="operatingEndTime"
                        type="time"
                        register={register}
                        errors={errors}
                        validation={{ required: 'Operating end time is mandatory' }}
                    />
                </div>
                <Button onClick={handleSubmit(onSubmit)}>
                    Submit
                </Button>
            </form>
        </div>
    )
}

export default AddGames
