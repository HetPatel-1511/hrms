import { useEffect } from 'react'
import Button from '../components/Button'
import { useForm } from 'react-hook-form';
import FormInput from '../components/TextInput';
import useUpdateGameConfigurationMutation from '../query/queryHooks/useUpdateGameConfigurationMutation';
import useSingleGameQuery from '../query/queryHooks/useSingleGameQuery';
import { useNavigate, useParams } from 'react-router';
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'

const EditGames = () => {
    const { gameId } = useParams();
    const navigate = useNavigate();
    
    const {
        register,
        handleSubmit,
        formState: { errors },
        setValue
    } = useForm();

    const updateGameConfiguration = useUpdateGameConfigurationMutation()
    const { data, isLoading, isError } = useSingleGameQuery(gameId!);

    const onSubmit = (data: any) => {
        updateGameConfiguration.mutate({ gameId, data });
    };

    useEffect(() => {
        if (updateGameConfiguration.isSuccess) {
            navigate('/games')
        }
    }, [updateGameConfiguration.isSuccess])

    useEffect(() => {
        if (data?.data?.gameConfiguration) {
            const config = data?.data.gameConfiguration;
            setValue("slotReleaseTime", config.slotReleaseTime);
            setValue("slotDurationMinutes", config.slotDurationMinutes);
            setValue("maxPlayersPerSlot", config.maxPlayersPerSlot);
            setValue("operatingStartTime", config.operatingStartTime);
            setValue("operatingEndTime", config.operatingEndTime);
            setValue("active", data?.data?.active);
        }
    }, [data?.data])

    if (isLoading) {
        return <Loading />
    }
    if (isError) {
        return <ServerError />
    }

    return (
        <div>
            <h1 className='text-2xl mb-4'>Edit Game Configuration - {data?.data?.name}</h1>
            <form className="">
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
                <div className="mb-5">
                    <FormInput
                        label="Status"
                        id="active"
                        type="select"
                        register={register}
                        errors={errors}
                        validation={{ required: 'Status is mandatory' }}
                        options={[
                            { value: true, label: 'Active' },
                            { value: false, label: 'Inactive' }
                        ]}
                    />
                </div>
                <Button onClick={handleSubmit(onSubmit)}>
                    Update Configuration
                </Button>
            </form>
        </div>
    )
}

export default EditGames
