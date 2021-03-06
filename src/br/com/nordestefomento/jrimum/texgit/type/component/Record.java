package br.com.nordestefomento.jrimum.texgit.type.component;

import static br.com.nordestefomento.jrimum.utilix.ObjectUtil.isNotNull;
import static br.com.nordestefomento.jrimum.utilix.ObjectUtil.isNull;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.com.nordestefomento.jrimum.texgit.type.Field;


@SuppressWarnings("serial")
public class Record extends BlockOfFields implements br.com.nordestefomento.jrimum.texgit.Record{

	private String name;
	
	private String description;
	
	private FixedField<String> idType;
	
	private FixedField<Long> sequencialNumber;
	
	private boolean headOfGroup;
	
	private List<br.com.nordestefomento.jrimum.texgit.Record> innerRecords;
	
	private Set<String> repitablesRecords;
	
	private List<String> declaredInnerRecords;
	
	public Record() {
		super();
	}
	
	/**
	 * @param length
	 * @param size
	 */
	public Record(Integer length, Integer size) {
		super(length, size);
	}

	@Override
	public Record clone() throws CloneNotSupportedException {
		//TODO Outros atributos
		return (Record) super.clone();
	}
	
	public FixedField<String> readID(String lineRecord) {

		FixedField<String> ffID = null;
		
		try {
			
			ffID = getIdType().clone();
			ffID.setName("");
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		getIdType().read(lineRecord.substring(getIdPosition(), getIdPosition() + getIdType().getFixedLength())); 

		return ffID;
	}
	
	public br.com.nordestefomento.jrimum.texgit.type.FixedField<?> getField(String fieldName) {

		br.com.nordestefomento.jrimum.texgit.type.FixedField<?> field = null;

		if (isNotBlank(fieldName))
			if (!getFields().isEmpty())
				for (FixedField<?> ff : this.getFields())
					if (ff.getName().equals(fieldName)) {
						field = ff;
						break;
					}

		return field;
	}

	public boolean isMyField(String idName){
		boolean is = false;
		
		if (isNotBlank(idName)) {
			if(!getFields().isEmpty())
				for(br.com.nordestefomento.jrimum.texgit.type.Field<?> f : getFields())
					if(idName.equals(f.getName())){
						is = true;
						break;
					}
		}
		return is;
	}
	
	private int getIdPosition(){
		int pos = 0;
		
		for(FixedField<?> ff : this.getFields())
			if(!ff.getName().equals(idType.getName()))
				pos += ff.getFixedLength();
			else
				break;
		
		return pos;
	}
	
	public int readInnerRecords(List<String> lines, int lineIndex, RecordFactory<Record> iFactory) {
		
		return readInnerRecords(this,lines,lineIndex,iFactory);
	}
	
	private int readInnerRecords(Record record, List<String> lines, int lineIndex, RecordFactory<Record> iFactory) {
		
		if(isNotNull(record)){
			if(isNotNull(record.getDeclaredInnerRecords()) && !record.getDeclaredInnerRecords().isEmpty()){
				
				boolean read = true;
				String line = null;
				
				FixedField<String> typeRecord = null;
				Record innerRec = null;
				
				for(String id : record.getDeclaredInnerRecords()){
					
					innerRec = iFactory.create(id);
					
					if(isRepitable(id)){
						
						while(read){
							
							if(isNull(innerRec))
								innerRec = iFactory.create(id);
							
							if(lineIndex < lines.size())
								line = lines.get(lineIndex);
							
							typeRecord = innerRec.readID(line);
							
							read = innerRec.getIdType().getValue().equals(typeRecord.getValue()) && (lineIndex < lines.size()); 

							if(read){
								
								innerRec.read(line);
								lineIndex++;
								record.addInnerRecord(innerRec);
								
								if(innerRec.isHeadOfGroup())
									innerRec.readInnerRecords(lines,lineIndex,iFactory);
								
								innerRec = null;
							}
						}
						
					}else{
						if((lineIndex < lines.size())){
							
							line = lines.get(lineIndex);
							typeRecord = innerRec.readID(line);
							
							if(innerRec.getIdType().getValue().equals(typeRecord.getValue())){
								
								innerRec.read(line);
								lineIndex++;
								record.addInnerRecord(innerRec);
								
								if(innerRec.isHeadOfGroup())
									innerRec.readInnerRecords(lines,lineIndex,iFactory);
								
								innerRec = null;
							}
						}
					}
				}
			}		
		}
		
		return lineIndex;
	}

	public List<String> writeInnerRecords(){
		
		return writeInnerRecords(this);
	}
	
	private List<String> writeInnerRecords(Record record){

		ArrayList<String> out = new ArrayList<String>(record.getInnerRecords().size());
		
		for(String id : getDeclaredInnerRecords()){//ordem
			
			if(isRepitable(id)){
					
				for(Record rec : getRecords(id)){
					
					out.add(rec.write()+"\r\n");
					
					if(rec.isHeadOfGroup())
						out.addAll(rec.writeInnerRecords());
				}
				
			}else{
				
				Record rec = getRecord(id);

				out.add(rec.write()+"\r\n");
				
				if(rec.isHeadOfGroup())
					out.addAll(rec.writeInnerRecords());
			}
		}
		
		return out;
	}
	
	public Record getRecord(String idName){
		
		Record record = null;
		
		if (isNotBlank(idName)) {
			if (!isRepitable(idName)){	
				if (!getInnerRecords().isEmpty()) {
					for (br.com.nordestefomento.jrimum.texgit.Record iRec : getInnerRecords()) {
						Record rec = (Record) iRec;
						if (idName.equals(rec.getName()))
							record = rec;
					}
				}
			}
		}

		return record;
	}

	public List<Record> getRecords(String idName) {

		List<Record> secRecords = new ArrayList<Record>();

		if (isNotBlank(idName)) {
			if (isRepitable(idName)) {
				if (!getInnerRecords().isEmpty()) {
					for (br.com.nordestefomento.jrimum.texgit.Record iRec : getInnerRecords()) {
						Record rec = (Record) iRec;
						if (idName.equals(rec.getName()))
							secRecords.add(rec);
					}
				}
			}
		}

		return secRecords;
	}
	
	public boolean isRepitable(String idName){
		
		return (isNotNull(repitablesRecords) && !repitablesRecords.isEmpty() && repitablesRecords.contains(idName));
	}
	
	public boolean isMyRecord(String idName){
		boolean is = false;
		
		if (isNotBlank(idName)) {
			if(!getDeclaredInnerRecords().isEmpty())
				if(getDeclaredInnerRecords().contains(idName))
					is = true;
		}
		return is;
	}
	
	@Override
	public void addInnerRecord(br.com.nordestefomento.jrimum.texgit.Record record) {
		
		if(isNotNull(record)){
			if(isNull(this.innerRecords))
				this.innerRecords = new ArrayList<br.com.nordestefomento.jrimum.texgit.Record>();
		
		if(isMyRecord(Record.class.cast(record).getName()))
			this.innerRecords.add(record);
		else
			throw new IllegalArgumentException("Record fora de scopo!");
		
		}
	}

	@Override
	public List<br.com.nordestefomento.jrimum.texgit.Record> getInnerRecords() {
		
		return this.innerRecords;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <G> G getValue(String fieldName) {
		
		G value = null;
		
		br.com.nordestefomento.jrimum.texgit.type.Field f = getField(fieldName);
		
		if(isNotNull(f))
			value = (G) f.getValue();
		
		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <G> void setValue(String fieldName, G value) {
		
		br.com.nordestefomento.jrimum.texgit.type.Field<G> f = (Field<G>) getField(fieldName);
		
		if(isNotNull(f))
			f.setValue(value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FixedField<String> getIdType() {
		return idType;
	}

	public void setIdType(FixedField<String> idType) {
		this.idType = idType;
	}

	public FixedField<Long> getSequencialNumber() {
		return sequencialNumber;
	}

	public void setSequencialNumber(FixedField<Long> sequencialNumber) {
		this.sequencialNumber = sequencialNumber;
	}

	public boolean isHeadOfGroup() {
		return headOfGroup;
	}

	public void setHeadOfGroup(boolean headOfGroup) {
		this.headOfGroup = headOfGroup;
	}

	public List<String> getDeclaredInnerRecords() {
		return declaredInnerRecords;
	}

	public void setDeclaredInnerRecords(List<String> declaredInnerRecords) {
		this.declaredInnerRecords = declaredInnerRecords;
	}
	
	public Set<String> getRepitablesRecords() {
		return repitablesRecords;
	}

	public void setRepitablesRecords(Set<String> repitablesRecords) {
		this.repitablesRecords = repitablesRecords;
	}

}