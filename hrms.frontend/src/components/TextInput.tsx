import Select from 'react-select';

const FormInput = ({
    type = "text",
    label,
    id,
    register,
    errors,
    placeholder,
    validation = {},
    options = [],
    onChange,
    isMult= true,
    ...props
}: any) => {
    return (
        <div className="mb-5">
            <label htmlFor={id} className="block mb-2.5 text-sm font-medium text-heading">
                {label}
            </label>
            {type == "multi-select" ?
                <Select
                    isMulti={isMult}
                    name={id}
                    id={id}
                    options={options}
                    className="basic-multi-select"
                    onMenuOpen={() => { }}
                    onChange={onChange}
                /> :
                type == "select" ?
                <select
                    id={id}
                    {...register(id, validation)}
                    className="bg-neutral-secondary-medium border border-default-medium text-heading text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-3 py-2.5 shadow-xs placeholder:text-body"
                >
                    <option value="">Select {label}</option>
                    {options.map((option: any) => (
                        <option key={option.value} value={option.value}>
                            {option.label}
                        </option>
                    ))}
                </select> :
                <input
                    type={type}
                    id={id}
                    {...register(id, validation)}
                    className="bg-neutral-secondary-medium border border-default-medium text-heading text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-3 py-2.5 shadow-xs placeholder:text-body"
                    placeholder={placeholder}
                    {...props}
                />
            }
            {errors[id] && (
                <span style={{ color: 'red' }} className="text-sm">
                    {errors[id].message?.toString()}
                </span>
            )}
        </div>
    );
};

export default FormInput;
